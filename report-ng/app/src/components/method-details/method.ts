import {autoinject, PLATFORM} from 'aurelia-framework';
import {NavigationInstruction, RouteConfig, Router, RouterConfiguration} from "aurelia-router";
import {IMethodDetails, StatisticsGenerator} from "../../services/statistics-generator";
import {data} from "../../services/report-model";
import {ScreenshotsDialog} from "../screenshots-dialog/screenshots-dialog";
import {MdcDialogService} from '@aurelia-mdc-web/dialog';
import IFile = data.IFile;

@autoinject()
export class Method {
    private _router:Router;
    private _screenshots:IFile[];
    private _lastScreenshot:IFile;
    private _methodDetails:IMethodDetails;

    constructor(
        private _statistics: StatisticsGenerator,
        private _dialogService:MdcDialogService,
    ) {
    }

    configureRouter(config: RouterConfiguration, router: Router) {
        this._router = router;
        config.map([
            {
                route: '',
                moduleId: PLATFORM.moduleName('t-systems-aurelia-components/src/components/empty/empty'),
            },
            {
                route: 'details',
                moduleId: PLATFORM.moduleName('./details'),
                nav: true,
                name: "details",
                title: 'Details',
                settings: {
                    icon: "center_focus_strong"
                }
            },
            {
                route: 'assertions',
                moduleId: PLATFORM.moduleName('./assertions'),
                nav: true,
                name: "assertions",
                title: 'Assertions',
                settings: {
                    icon: "rule",
                }
            },
            {
                route: 'steps',
                moduleId: PLATFORM.moduleName('./steps'),
                nav: true,
                name: "steps",
                title: 'Steps',
                settings: {
                    icon: "list",
                }
            },
            {
                route: 'videos',
                moduleId: PLATFORM.moduleName('./videos'),
                nav: true,
                name: "videos",
                title: 'Videos',
                settings: {
                    icon: "videocam"
                }
            },
            {
                route: 'dependencies',
                moduleId: PLATFORM.moduleName('./dependency-network'),
                nav: true,
                name: "dependencies",
                title: 'Dependencies',
                settings: {
                    icon: "account_tree",
                    // icon: "sync_alt"
                }
            },
        ]);
    }

    activate(
        params: any,
        routeConfig: RouteConfig,
        navInstruction: NavigationInstruction
    ) {
        this._statistics.getMethodDetails(params.methodId).then(methodDetails => {
            this._methodDetails = methodDetails;
            this._statistics.getScreenshotsFromMethodContext(methodDetails.methodContext).then(screenshots => {
                this._screenshots = screenshots;
                this._lastScreenshot = this._screenshots.find(() => true);
            })

            this._router.routes.forEach(routeConfig => {
                switch (routeConfig.name) {
                    case "steps": {
                        routeConfig.settings.count = methodDetails.methodContext.testSteps.length;
                        break;
                    }
                    case "dependencies": {
                        const count = methodDetails.methodContext.relatedMethodContextIds.length + methodDetails.methodContext.dependsOnMethodContextIds.length;
                        if (count > 0) {
                            routeConfig.nav = true;
                            routeConfig.settings.count = count;
                        } else {
                            routeConfig.nav = false;
                        }
                        break;
                    }
                    case "videos": {
                        if (methodDetails.methodContext.videoIds.length == 0) {
                            routeConfig.nav = false;
                        } else {
                            routeConfig.settings.count = methodDetails.methodContext.videoIds.length;
                            routeConfig.nav = true;
                        }
                        break;
                    }
                    case "details": {
                        if (methodDetails.numDetails > 0) {
                            routeConfig.nav = true;
                            routeConfig.settings.count = methodDetails.numDetails;
                        } else {
                            routeConfig.nav = false;
                        }
                        break;
                    }
                    case "assertions": {
                        let allCollected = 0;
                        let allOptional = 0;
                        methodDetails.methodContext.testSteps
                            .flatMap(testStep => testStep.testStepActions)
                            .forEach(testStepAction => {
                                allCollected += testStepAction.collectedAssertions.length;
                                allOptional += testStepAction.optionalAssertions.length;
                            });
                        if (allCollected > 0 ||allOptional > 0) {
                            routeConfig.nav = true;
                            routeConfig.settings.count = `${allCollected}/${allOptional}`;
                        } else {
                            routeConfig.nav = false;
                        }
                        break;
                    }
                }
            });

            if (!routeConfig.hasChildRouter) {
                const navOptions = {replace:true};
                const enabledRouteConfig = this._router.routes.find(routeConfig => routeConfig.nav);
                this._router.navigateToRoute(enabledRouteConfig.name, {}, navOptions);
            }
        });
    }

    private _tabClicked(routeConfig:RouteConfig) {
        this._router.navigateToRoute(routeConfig.name);
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
}
