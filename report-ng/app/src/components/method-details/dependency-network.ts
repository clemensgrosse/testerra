import {autoinject} from 'aurelia-framework';
import {IMethodDetails, StatisticsGenerator} from "../../services/statistics-generator";
import {NavigationInstruction, RouteConfig, Router} from "aurelia-router";
import {DataSet, Network, DataSetNodes, DataSetEdges} from "vis-network";
import "vis-network/styles/vis-network.css";
import {data} from "../../services/report-model";
import IMethodContext = data.IMethodContext;
import {StatusConverter} from "../../services/status-converter";
import {Options} from "vis-network/dist/types/network/Network";

@autoinject()
export class DependencyNetwork {
    private _router:Router;
    private _container:HTMLDivElement;
    private _resizeListener:EventListener;
    private _queryParams;
    private _network:Network;
    private _methodDetails:IMethodDetails;
    private _loadListener:EventListener;

    constructor(
        private _statistics: StatisticsGenerator,
        private _statusConverter:StatusConverter,
    ) {
        this._resizeListener = (ev:Event) => {
            this._container.style.height = (window.innerHeight - this._container.getBoundingClientRect().y - 24) + "px";
            this._network?.redraw();
        }

        this._loadListener = (ev:Event) => {
            console.log("window load");
            this._resizeListener(ev);
            this._focus();
        }
    }

    private _focus() {
        this._network?.focus(this._methodDetails.methodContext.contextValues.id, {
            scale: 2
        });
    }

    private _generateRoute(methodDetails:IMethodDetails) {
        return this._router?.generate("method", {methodId:methodDetails.methodContext.contextValues.id})+"/dependencies";
    }

    attached() {
        console.log("attached");
        this._createGraph();

        window.addEventListener("resize", this._resizeListener);
        window.addEventListener("load", this._loadListener);
    }

    detached() {
        window.removeEventListener("resize", this._resizeListener);
        window.removeEventListener("load", this._loadListener);
    }

    activate(
        params: any,
        routeConfig: RouteConfig,
        navInstruction: NavigationInstruction
    ) {
        console.log("activate");
        this._router = navInstruction.router;
        this._queryParams = params;
        this._statistics.getMethodDetails(this._queryParams.methodId).then(methodDetails => {
            this._methodDetails = methodDetails;
            this._createGraph();
        });
    }

    private _createGraph() {
        if (!this._methodDetails || !this._container) {
            return;
        }

        console.log("build graph");

        const methodDetails = this._methodDetails;

        const nodes:DataSetNodes[] = [];
        const edges:DataSetEdges[] = [];

        const addNode = (methodContext:IMethodContext) => {
            const node:DataSetNodes = {
                id: methodContext.contextValues.id,
                label: methodContext.contextValues.name,
                physics: false,
                color: this._statusConverter.getColorForStatus(methodContext.contextValues.resultStatus),
                font: {
                    color: "#fff",
                    face: "Roboto, sans-serif",
                },
                value: 1,
            };
            nodes.push(node);
            return node;
        }

        const main = addNode(methodDetails.methodContext);
        //main.mass = 10;
        main.value = 3;

        methodDetails.methodContext.dependsOnMethodContextIds.forEach(methodId => {
            const methodContext = methodDetails.classStatistics.methodContexts.find(methodContext => methodContext.contextValues.id == methodId);
            addNode(methodContext);
            edges.push({
                from: methodContext.contextValues.id,
                to: methodDetails.methodContext.contextValues.id,
            })
        })

        methodDetails.methodContext.relatedMethodContextIds.forEach(methodId => {
            /**
             * @todo Remove this workaround when data model is fixed
             */
            if (methodId != methodDetails.methodContext.contextValues.id) {
                const methodContext = methodDetails.classStatistics.methodContexts.find(methodContext => methodContext.contextValues.id == methodId);
                addNode(methodContext);
                edges.push({
                    from: methodDetails.methodContext.contextValues.id,
                    to: methodContext.contextValues.id,
                })
            }
        });

        // create a network
        const data = {
            nodes: new DataSet(nodes),
            edges: new DataSet(edges),
        };
        const options:Options = {
            autoResize: false,
            nodes: {
                shape: 'box',
                scaling:{
                    min:5,
                    max:100,
                    label: {
                        min:8,
                        max:12
                    }
                }
            },
            edges: {
                arrows: {
                    to: {
                        enabled: true
                    }
                }
            },
            layout: {
                hierarchical: {
                    //nodeSpacing: 1,
                    levelSeparation: 100,
                    direction: "UD",
                    sortMethod: "directed",
                },
            },
        };
        this._resizeListener(null);
        this._network?.destroy();
        this._network = new Network(this._container, data, options);
        this._network.on("click", (params) => {
            if (params.nodes.length > 0) {
                this._router.navigate(this._router.generate("method", {methodId:params.nodes[0]})+"/dependencies");
            }
        });
        this._focus();
    }
}
