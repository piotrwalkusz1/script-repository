import { Route } from '@angular/router';
import {RepositoryScriptEditComponent} from './repository-script-edit.component';
import {UserRouteAccessService} from '../../shared';

export const REPOSITORY_SCRIPT_EDIT_ROUTE: Route = {
    path: 'repository/scripts/:scriptId/edit',
    component: RepositoryScriptEditComponent,
    canActivate: [UserRouteAccessService],
    data: {
        authorities: ['ROLE_USER']
    }
};

export const REPOSITORY_SCRIPT_NEW_ROUTE: Route = {
    path: 'repository/collections/:collectionId/scripts/new',
    component: RepositoryScriptEditComponent,
    canActivate: [UserRouteAccessService],
    data: {
        authorities: ['ROLE_USER']
    }
};
