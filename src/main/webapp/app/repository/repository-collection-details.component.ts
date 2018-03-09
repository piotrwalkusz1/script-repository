import {Component, OnInit, Input, OnChanges, SimpleChanges} from '@angular/core';
import {Collection} from '../entities/collection';
import {TranslateService} from '@ngx-translate/core';
import {Script} from '../entities/script';
import {RepositoryService} from './repository.service';
import {HttpResponse, HttpErrorResponse} from '@angular/common/http';
import {JhiAlertService} from 'ng-jhipster';

@Component({
    selector: 'jhi-repository-collection-details',
    templateUrl: './repository-collection-details.component.html',
    styleUrls: ['repository.scss']
})
export class RepositoryCollectionDetailsComponent implements OnInit, OnChanges {

    @Input() collection: Collection;
    scripts: Script[] = [];

    constructor(private translateService: TranslateService,
                private repositoryService: RepositoryService,
                private jhiAlertService: JhiAlertService) {}

    ngOnInit(): void {
        this.collectionChanged();
    }

    ngOnChanges(changes: SimpleChanges): void {
        this.collectionChanged();
    }

    collectionChanged() {
        this.scripts = [];
        if (this.collection) {
            this.repositoryService.getAllScriptsFromCollection(this.collection.id).subscribe(
                (res: HttpResponse<Script[]>) => {
                    this.scripts = res.body;
                },
                (res: HttpErrorResponse) => {
                    this.onError(res.message);
                }
            );
        }
    }

    getColorFromScriptLanguage(language: String): String {
        switch (language) {
            case 'BASH': return 'darkgoldenrod';
            case 'GROOVY': return 'green';
            case 'PYTHON_2': return 'darkcyan';
            case 'PYTHON_3': return 'darkgrey';
            case 'KSCRIPT': return 'orange';
            case 'RUBY': return 'red';
            default: return 'red';
        }
    }

    private onError(error) {
        this.jhiAlertService.error(error.message, null, null);
    }
}
