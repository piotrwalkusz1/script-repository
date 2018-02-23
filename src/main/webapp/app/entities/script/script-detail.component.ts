import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager, JhiDataUtils } from 'ng-jhipster';

import { Script } from './script.model';
import { ScriptService } from './script.service';

@Component({
    selector: 'jhi-script-detail',
    templateUrl: './script-detail.component.html'
})
export class ScriptDetailComponent implements OnInit, OnDestroy {

    script: Script;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private dataUtils: JhiDataUtils,
        private scriptService: ScriptService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInScripts();
    }

    load(id) {
        this.scriptService.find(id)
            .subscribe((scriptResponse: HttpResponse<Script>) => {
                this.script = scriptResponse.body;
            });
    }
    byteSize(field) {
        return this.dataUtils.byteSize(field);
    }

    openFile(contentType, field) {
        return this.dataUtils.openFile(contentType, field);
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInScripts() {
        this.eventSubscriber = this.eventManager.subscribe(
            'scriptListModification',
            (response) => this.load(this.script.id)
        );
    }
}
