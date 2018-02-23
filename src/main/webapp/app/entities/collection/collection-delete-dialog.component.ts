import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Collection } from './collection.model';
import { CollectionPopupService } from './collection-popup.service';
import { CollectionService } from './collection.service';

@Component({
    selector: 'jhi-collection-delete-dialog',
    templateUrl: './collection-delete-dialog.component.html'
})
export class CollectionDeleteDialogComponent {

    collection: Collection;

    constructor(
        private collectionService: CollectionService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.collectionService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'collectionListModification',
                content: 'Deleted an collection'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-collection-delete-popup',
    template: ''
})
export class CollectionDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private collectionPopupService: CollectionPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.collectionPopupService
                .open(CollectionDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
