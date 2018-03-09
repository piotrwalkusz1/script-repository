import {Script, ScriptLanguage} from '../entities/script';
import {OnInit, Component, Input, OnChanges, SimpleChanges, AfterViewChecked} from '@angular/core';
import {RepositoryService} from './repository.service';
import {HttpResponse, HttpErrorResponse} from '@angular/common/http';
import {JhiAlertService} from 'ng-jhipster';
import {ActivatedRoute} from '@angular/router';

let Prism = require('prismjs');


@Component({
    selector: 'jhi-repository-script',
    templateUrl: './repository-script.component.html'
})
export class RepositoryScriptComponent implements OnInit, OnChanges, AfterViewChecked {


    @Input() id: number;

    script: Script;

    constructor(route: ActivatedRoute,
                private repositoryService: RepositoryService,
                private jhiAlertService: JhiAlertService) {
       this.id = route.snapshot.params['id'];
    }

    ngOnInit() {
        this.loadAll();
    }

    ngOnChanges(changes: SimpleChanges) {
        this.loadAll();
    }

    ngAfterViewChecked() {
        Prism.highlightAll();
    }

    loadAll() {
        if (this.id) {
            this.repositoryService.getScript(this.id).subscribe(
                (res: HttpResponse<Script>) => {
                    this.script = res.body;
                }, (res: HttpErrorResponse) => {
                    this.onError(res.message);
                }
            );
        }
    }

    getLanguageClass(): String {
        switch (String(this.script.scriptLanguage)) {
            case 'BASH': return 'language-bash';
            case 'GROOVY': return 'language-groovy';
            case 'PYTHON_2': return 'language-python';
            case 'PYTHON_3': return 'language-python';
            case 'KSCRIPT': return 'language-kotlin';
            case 'RUBY': return 'language-ruby';
            default: return 'language-unknown';
        }
    }

    private onError(error) {
        this.jhiAlertService.error(error.message, null, null);
    }
}
