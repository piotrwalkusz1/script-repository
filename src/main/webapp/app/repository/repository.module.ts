import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { RepositoryComponent, REPOSITORY_ROUTE } from './';
import {ScriptRepositorySharedModule} from '../shared';
import {RepositoryService} from './repository.service';
import {RepositoryCollectionComponent} from './repository-collection.component';

@NgModule({
    imports: [
        RouterModule.forChild([REPOSITORY_ROUTE]),
        ScriptRepositorySharedModule
    ],
    declarations: [
        RepositoryComponent,
        RepositoryCollectionComponent
    ],
    entryComponents: [],
    providers: [
        RepositoryService
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ScriptRepositoryRepositoryModule {}
