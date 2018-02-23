import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService, JhiDataUtils } from 'ng-jhipster';

import { Script } from './script.model';
import { ScriptPopupService } from './script-popup.service';
import { ScriptService } from './script.service';
import { Collection, CollectionService } from '../collection';
import { Tag, TagService } from '../tag';

@Component({
    selector: 'jhi-script-dialog',
    templateUrl: './script-dialog.component.html'
})
export class ScriptDialogComponent implements OnInit {

    script: Script;
    isSaving: boolean;

    collections: Collection[];

    tags: Tag[];

    constructor(
        public activeModal: NgbActiveModal,
        private dataUtils: JhiDataUtils,
        private jhiAlertService: JhiAlertService,
        private scriptService: ScriptService,
        private collectionService: CollectionService,
        private tagService: TagService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.collectionService.query()
            .subscribe((res: HttpResponse<Collection[]>) => { this.collections = res.body; }, (res: HttpErrorResponse) => this.onError(res.message));
        this.tagService.query()
            .subscribe((res: HttpResponse<Tag[]>) => { this.tags = res.body; }, (res: HttpErrorResponse) => this.onError(res.message));
    }

    byteSize(field) {
        return this.dataUtils.byteSize(field);
    }

    openFile(contentType, field) {
        return this.dataUtils.openFile(contentType, field);
    }

    setFileData(event, entity, field, isImage) {
        this.dataUtils.setFileData(event, entity, field, isImage);
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.script.id !== undefined) {
            this.subscribeToSaveResponse(
                this.scriptService.update(this.script));
        } else {
            this.subscribeToSaveResponse(
                this.scriptService.create(this.script));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<Script>>) {
        result.subscribe((res: HttpResponse<Script>) =>
            this.onSaveSuccess(res.body), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess(result: Script) {
        this.eventManager.broadcast({ name: 'scriptListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }

    trackCollectionById(index: number, item: Collection) {
        return item.id;
    }

    trackTagById(index: number, item: Tag) {
        return item.id;
    }

    getSelected(selectedVals: Array<any>, option: any) {
        if (selectedVals) {
            for (let i = 0; i < selectedVals.length; i++) {
                if (option.id === selectedVals[i].id) {
                    return selectedVals[i];
                }
            }
        }
        return option;
    }
}

@Component({
    selector: 'jhi-script-popup',
    template: ''
})
export class ScriptPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private scriptPopupService: ScriptPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.scriptPopupService
                    .open(ScriptDialogComponent as Component, params['id']);
            } else {
                this.scriptPopupService
                    .open(ScriptDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
