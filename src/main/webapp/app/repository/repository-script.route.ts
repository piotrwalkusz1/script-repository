import { Route } from '@angular/router';

import { RepositoryComponent } from './repository.component';
import {RepositoryScriptComponent} from "./repository-script.component";
import {UserRouteAccessService} from "../shared";

export const REPOSITORY_SCRIPT_ROUTE: Route = {
    path: 'repository/scripts/:id',
    component: RepositoryScriptComponent,
    canActivate: [UserRouteAccessService],
    data: {
        authorities: ['ROLE_USER']
    }
};
