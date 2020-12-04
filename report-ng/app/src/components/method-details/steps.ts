import {autoinject} from 'aurelia-framework';
import {IMethodDetails, StatisticsGenerator} from "../../services/statistics-generator";
import {data} from "../../services/report-model";
import {DataLoader} from "../../services/data-loader";
import {Config} from "../../services/config";
import {MdcDialogService} from '@aurelia-mdc-web/dialog';
import {ScreenshotsDialog} from "../screenshots-dialog/screenshots-dialog";
import {NavigationInstruction, RouteConfig, Router} from "aurelia-router";
import IFile = data.IFile;
import './steps.scss'

@autoinject()
export class Steps {
    private _methodDetails:IMethodDetails;
    private _screenshots:IFile[] = [];
    private _router:Router;
    private _sticky:boolean;

    constructor(
        private _statistics: StatisticsGenerator,
        private _dataLoader:DataLoader,
        private _config:Config,
        private _dialogService:MdcDialogService,
    ) {

    }

    activate(
        params: any,
        routeConfig: RouteConfig,
        navInstruction: NavigationInstruction
    ) {
        this._router = navInstruction.router;
        this._statistics.getMethodDetails(params.methodId).then(methodDetails => {
            this._methodDetails = methodDetails;
            this._statistics.getScreenshotsFromMethodContext(methodDetails.methodContext).then(screenshots => {
                screenshots.forEach(screenshot => {
                    this._screenshots.push(screenshot);
                })
            })
        });
    }

    private _showScreenshot(file:data.File) {
        this._dialogService.open({
            viewModel: ScreenshotsDialog,
            model: {
                current: file,
                screenshots: this._screenshots
            },
            class: "screenshot-dialog"
        });
    }

    private _findScreenshot(id:string) {
        return this._screenshots?.find(value => value.id == id);
    }

    private _gotoStep(stepIndex:number, actionIndex:number=0) {
        let elementsByName = window.document.getElementsByName(stepIndex+(actionIndex>0?"."+actionIndex:""));
        if (elementsByName.length > 0) {
            elementsByName[0].scrollIntoView()
        }
    }

    private _stepVisibility($event:CustomEvent) {
        // const anchor = $event.target as HTMLAnchorElement;
        // const parts = anchor.getAttribute("name").split(".");
        // console.log("enable", parts);
    }

    private _stickyStepNav($event:CustomEvent) {
        this._sticky = $event.detail;
    }
}
