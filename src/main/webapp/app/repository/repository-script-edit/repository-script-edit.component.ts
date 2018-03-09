import { Component, OnInit, Input, SimpleChanges, AfterViewInit, AfterViewChecked } from '@angular/core';
import {RepositoryService} from '../repository.service';
import {Script, ScriptLanguage} from '../../entities/script';
import {ActivatedRoute, Router} from '@angular/router';
import {JhiAlertService} from 'ng-jhipster';
import {HttpResponse, HttpErrorResponse} from '@angular/common/http';
import {Collection} from '../../entities/collection';

const $ = require('jquery');

const CodeMirror = require('codemirror');

@Component({
  selector: 'jhi-repository-script-edit',
  templateUrl: './repository-script-edit.component.html',
  styles: []
})
export class RepositoryScriptEditComponent implements OnInit, AfterViewChecked {

    @Input() id: number;

    script: Script;
    isSaving: Boolean;
    collections: Collection[];

    private codeEditor = null;

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

    ngAfterViewChecked() {
        if (!this.codeEditor) {
            let codeArea = $('#field_code')[0];
            if (codeArea) {
                this.codeEditor = CodeMirror.fromTextArea(codeArea, {mode: this.getModeFromScriptLanguage(this.script.scriptLanguage)});
                this.codeEditor.setValue(this.script.code);
            }
        }
    }

    loadAll() {
        if (!this.isNew()) {
            this.repositoryService.getScript(this.id).subscribe(
                (res: HttpResponse<Script>) => {
                    this.script = res.body;
                }, (res: HttpErrorResponse) => {
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
            );
        }
    }

    isNew() {
        return this.id === null;
    }

    save() {
        this.isSaving = true;
        this.script.code = this.codeEditor.getValue();
        if (this.id) {
            this.repositoryService.updateScript(this.script).subscribe(
                () => {
                    this.isSaving = false;
                    this.router.navigateByUrl('/repository/scripts/' + this.id);
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
            );
        }

    }

    onScriptLanguageChanged() {
        console.log("chenged");
        this.codeEditor.setOption("mode", this.getModeFromScriptLanguage(this.script.scriptLanguage));
    }

    private getModeFromScriptLanguage(language): string {
        switch (language) {
            case 'BASH': return 'shell';
            case 'RUBY': return 'ruby';
            case 'KSCRIPT': return 'clike';
            case 'PYTHON_3': return 'python';
            case 'PYTHON_2': return 'python';
            case 'GROOVY': return 'groovy';
            default: return '';
        }
    }

    private onError(error) {
        this.jhiAlertService.error(error.message, null, null);
    }
}
