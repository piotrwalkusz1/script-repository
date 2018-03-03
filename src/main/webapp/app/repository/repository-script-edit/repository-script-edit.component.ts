import { Component, OnInit, Input, SimpleChanges } from '@angular/core';
import {RepositoryService} from "../repository.service";
import {Script} from "../../entities/script";
import {ActivatedRoute, Route, Router} from '@angular/router';
import {JhiAlertService} from 'ng-jhipster';
import {HttpResponse, HttpErrorResponse} from '@angular/common/http';
import {Principal} from "../../shared";
import {Collection, CollectionService} from "../../entities/collection";

@Component({
  selector: 'jhi-repository-script-edit',
  templateUrl: './repository-script-edit.component.html',
  styles: []
})
export class RepositoryScriptEditComponent implements OnInit {

    @Input() id: number;

    script: Script;
    isSaving: Boolean;
    collections: Collection[];

    constructor(route: ActivatedRoute,
                private repositoryService: RepositoryService,
                private jhiAlertService: JhiAlertService,
                private router: Router) {
        if (route.snapshot.params['scriptId']) {
            this.id = route.snapshot.params['scriptId'];
        } else {
            this.id = null;
            this.script = new Script();
            this.script.collectionId = route.snapshot.params['collectionId'];
        }

        this.isSaving = false;
    }

    ngOnInit() {
        this.loadAll();
    }

    ngOnChanges(changes: SimpleChanges) {
        this.loadAll();
    }

    loadAll() {
        if (!this.isNew()) {
            this.repositoryService.getScript(this.id).subscribe(
                (res: HttpResponse<Script>) => {
                    this.script = res.body;
                },(res: HttpErrorResponse) => {
                    this.onError(res.message);
                }
            );

            this.repositoryService.getAllCollections().subscribe(
                (res: HttpResponse<Collection[]>) => {
                    this.collections = res.body;
                },
                (res: HttpErrorResponse) => {
                    this.onError(res.message);
                }
            )
        }
    }

    isNew() {
        return this.id === null;
    }

    save() {
        this.isSaving = true;
        if (this.id) {
            this.repositoryService.updateScript(this.script).subscribe(
                () => {
                    this.isSaving = false;
                },
                (res: HttpErrorResponse) => {
                    this.isSaving = false;
                    this.onError(res.message);
                }
            );
        } else {
            this.repositoryService.saveScript(this.script).subscribe(
                (res: HttpResponse<Script>) => {
                    this.router.navigateByUrl('/repository/scripts/' + res.body.id);
                },
                (res: HttpErrorResponse) => {
                    this.onError(res.message);
                }
            )
        }

    }

    private onError(error) {
        this.jhiAlertService.error(error.message, null, null);
    }
}
