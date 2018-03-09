import { Route } from '@angular/router';

import { RepositoryComponent } from './repository.component';
import {UserRouteAccessService} from '../shared';

export const REPOSITORY_ROUTES: [Route] = [
    {
        path: 'repository',
        component: RepositoryComponent,
        canActivate: [UserRouteAccessService],
        data: {
            authorities: ['ROLE_USER']
        }
    },
    {
        path: 'repository/:collectionId',
        component: RepositoryComponent,
        canActivate: [UserRouteAccessService],
        data: {
            authorities: ['ROLE_USER']
        }
    }
];
