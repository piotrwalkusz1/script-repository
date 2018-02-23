import { BaseEntity } from './../../shared';

export const enum Color {
    'RED',
    'BLUE',
    'GREEN',
    'YELLOW',
    'BLACK',
    'BROWN',
    'ORANGE',
    'DARK_BLUE',
    'VIOLET'
}

export class Tag implements BaseEntity {
    constructor(
        public id?: number,
        public name?: string,
        public color?: Color,
        public scripts?: BaseEntity[],
    ) {
    }
}
