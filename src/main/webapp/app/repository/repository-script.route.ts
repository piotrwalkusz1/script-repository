import { Route } from '@angular/router';

import { RepositoryComponent } from './repository.component';
import {RepositoryScriptComponent} from "./repository-script.component";

export const REPOSITORY_SCRIPT_ROUTE: Route = {
    path: 'repository/scripts/:id',
    component: RepositoryScriptComponent,
    data: {
        pageTitle: 'repository.title'
    }
};
