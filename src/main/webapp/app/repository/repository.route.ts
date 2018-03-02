import { Route } from '@angular/router';

import { RepositoryComponent } from './repository.component';

export const REPOSITORY_ROUTE: Route = {
    path: 'repository',
    component: RepositoryComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'repository.title'
    }
};
