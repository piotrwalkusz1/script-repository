import {Component, OnInit, Input} from '@angular/core';
import {Collection} from '../entities/collection';
import { TranslateService } from '@ngx-translate/core';

@Component({
    selector: 'jhi-repository-collection',
    templateUrl: './repository-collection.component.html'
})
export class RepositoryCollectionComponent {

    @Input() collection: Collection;

    constructor(private translateService: TranslateService) {}
}
