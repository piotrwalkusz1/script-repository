import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { RepositoryComponent, REPOSITORY_ROUTES } from './';
import {ScriptRepositorySharedModule} from '../shared';
import {RepositoryService} from './repository.service';
import {RepositoryCollectionDetailsComponent} from './repository-collection-details.component';
import {REPOSITORY_SCRIPT_ROUTE} from './repository-script.route';
import {RepositoryScriptComponent} from './repository-script.component';
import { RepositoryScriptEditComponent } from './repository-script-edit/repository-script-edit.component';
import {
    REPOSITORY_SCRIPT_EDIT_ROUTE,
    REPOSITORY_SCRIPT_NEW_ROUTE
} from './repository-script-edit/repository-script-edit.route';
import { RepositoryCollectionEditComponent } from './repository-collection-edit/repository-collection-edit.component';
import {
    REPOSITORY_COLLECTION_EDIT_ROUTE,
    REPOSITORY_COLLECTION_NEW_ROUTE
} from './repository-collection-edit/repository-collection-edit.route';

@NgModule({
    imports: [
        RouterModule.forChild([REPOSITORY_ROUTES[0]]),
        RouterModule.forChild([REPOSITORY_ROUTES[1]]),
        RouterModule.forChild([REPOSITORY_SCRIPT_ROUTE]),
        RouterModule.forChild([REPOSITORY_SCRIPT_EDIT_ROUTE]),
        RouterModule.forChild([REPOSITORY_SCRIPT_NEW_ROUTE]),
        RouterModule.forChild([REPOSITORY_COLLECTION_NEW_ROUTE]),
        RouterModule.forChild([REPOSITORY_COLLECTION_EDIT_ROUTE]),
        ScriptRepositorySharedModule
    ],
    declarations: [
        RepositoryComponent,
        RepositoryCollectionDetailsComponent,
        RepositoryScriptComponent,
        RepositoryScriptEditComponent,
        RepositoryCollectionEditComponent
    ],
    entryComponents: [],
    providers: [
        RepositoryService
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ScriptRepositoryRepositoryModule {}
