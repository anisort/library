import {BookItemModel} from './book-item.model';

export interface BookItemListModel {
  books: BookItemModel[];
  limit: number;
}
