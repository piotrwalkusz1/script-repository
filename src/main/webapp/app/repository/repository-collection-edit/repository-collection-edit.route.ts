import { Route } from '@angular/router';
import {UserRouteAccessService} from '../../shared';
import {RepositoryCollectionEditComponent} from './repository-collection-edit.component';

export const REPOSITORY_COLLECTION_EDIT_ROUTE: Route = {
    path: 'repository/collections/:collectionId/edit',
    component: RepositoryCollectionEditComponent,
    canActivate: [UserRouteAccessService],
    data: {
        authorities: ['ROLE_USER']
    }
};

export const REPOSITORY_COLLECTION_NEW_ROUTE: Route = {
    path: 'repository/collections/new',
    component: RepositoryCollectionEditComponent,
    canActivate: [UserRouteAccessService],
    data: {
        authorities: ['ROLE_USER']
    }
};
