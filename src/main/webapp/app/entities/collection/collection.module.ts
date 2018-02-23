import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { ScriptRepositorySharedModule } from '../../shared';
import { ScriptRepositoryAdminModule } from '../../admin/admin.module';
import {
    CollectionService,
    CollectionPopupService,
    CollectionComponent,
    CollectionDetailComponent,
    CollectionDialogComponent,
    CollectionPopupComponent,
    CollectionDeletePopupComponent,
    CollectionDeleteDialogComponent,
    collectionRoute,
    collectionPopupRoute,
} from './';

const ENTITY_STATES = [
    ...collectionRoute,
    ...collectionPopupRoute,
];

@NgModule({
    imports: [
        ScriptRepositorySharedModule,
        ScriptRepositoryAdminModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        CollectionComponent,
        CollectionDetailComponent,
        CollectionDialogComponent,
        CollectionDeleteDialogComponent,
        CollectionPopupComponent,
        CollectionDeletePopupComponent,
    ],
    entryComponents: [
        CollectionComponent,
        CollectionDialogComponent,
        CollectionPopupComponent,
        CollectionDeleteDialogComponent,
        CollectionDeletePopupComponent,
    ],
    providers: [
        CollectionService,
        CollectionPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ScriptRepositoryCollectionModule {}
