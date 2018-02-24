import { BaseEntity, User } from './../../shared';

export const enum Privacy {
    'PUBLIC',
    'PRIVATE'
}

export class Collection implements BaseEntity {
    constructor(
        public id?: number,
        public name?: string,
        public privacy?: Privacy,
        public ownerId?: number,
        public sharedUsers?: number[],
        public scripts?: BaseEntity[],
    ) {
    }
}
