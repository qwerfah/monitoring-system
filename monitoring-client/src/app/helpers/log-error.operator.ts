import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';

export const logError = <T extends unknown>(source$: Observable<T>) => {
  return source$.pipe(
    catchError((error) => {
      console.log('HTTP_ERROR', error);
      return throwError(error.error);
    })
  );
};
