export class BookStatusUtils {
  static getStatusClasses(bookStatus: string): Record<string, boolean> {
    return {
      'to-read': bookStatus === 'TO_READ',
      'reading': bookStatus === 'READING',
      'read': bookStatus === 'READ',
      'favorite': bookStatus === 'FAVORITE'
    };
  }

  static formatBookStatus(status: string): string {
    switch (status) {
      case 'TO_READ': return 'To read';
      case 'READING': return 'Reading';
      case 'READ': return 'Read';
      case 'FAVORITE': return 'Favorite';
      default: return status;
    }
  }
}
