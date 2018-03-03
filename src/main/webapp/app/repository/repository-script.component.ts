import {Collection} from "../entities/collection";
import {Script} from "../entities/script";
import {OnInit, Component, Input, OnChanges, SimpleChanges} from "@angular/core";
import {RepositoryService} from "./repository.service";
import {HttpResponse, HttpErrorResponse} from "@angular/common/http";
import {JhiAlertService} from "ng-jhipster";
import {ActivatedRoute} from "@angular/router";

@Component({
    selector: 'jhi-repository-script',
    templateUrl: './repository-script.component.html'
})
export class RepositoryScriptComponent implements OnInit, OnChanges {


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

    loadAll() {
        if (this.id) {
            this.repositoryService.getScript(this.id).subscribe(
                (res: HttpResponse<Script>) => {
                    this.script = res.body;
                },(res: HttpErrorResponse) => {
                    this.onError(res.message);
                }
            )
        }
    }

    private onError(error) {
        this.jhiAlertService.error(error.message, null, null);
    }
}
