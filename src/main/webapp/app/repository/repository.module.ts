import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { RepositoryComponent, REPOSITORY_ROUTE } from './';
import {ScriptRepositorySharedModule} from '../shared';
import {RepositoryService} from './repository.service';
import {RepositoryCollectionDetailsComponent} from './repository-collection-details.component';
import {REPOSITORY_SCRIPT_ROUTE} from "./repository-script.route";
import {RepositoryScriptComponent} from "./repository-script.component";
import { RepositoryScriptEditComponent } from './repository-script-edit/repository-script-edit.component';
import {
    REPOSITORY_SCRIPT_EDIT_ROUTE,
    REPOSITORY_SCRIPT_NEW_ROUTE
} from "./repository-script-edit/repository-script-edit.route";

@NgModule({
    imports: [
        RouterModule.forChild([REPOSITORY_ROUTE]),
        RouterModule.forChild([REPOSITORY_SCRIPT_ROUTE]),
        RouterModule.forChild([REPOSITORY_SCRIPT_EDIT_ROUTE]),
        RouterModule.forChild([REPOSITORY_SCRIPT_NEW_ROUTE]),
        ScriptRepositorySharedModule
    ],
    declarations: [
        RepositoryComponent,
        RepositoryCollectionDetailsComponent,
        RepositoryScriptComponent,
        RepositoryScriptEditComponent
    ],
    entryComponents: [],
    providers: [
        RepositoryService
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ScriptRepositoryRepositoryModule {}
