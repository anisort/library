import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

export function deleteBookWithReload(
  bookId: number,
  deleteFn: (id: number) => Observable<void>,
  reloadFn: () => void,
  setLoading: (loading: boolean) => void
) {
  setLoading(true);
  deleteFn(bookId)
    .pipe(finalize(() => setLoading(false)))
    .subscribe({
      next: () => reloadFn(),
      error: err => console.error(err)
    });
}
