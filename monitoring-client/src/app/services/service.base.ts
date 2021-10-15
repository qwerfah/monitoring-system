import { HttpErrorResponse } from '@angular/common/http';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Observable, ObservableInput, throwError } from 'rxjs';

export class ServiceBase {
  /** Base http request error handler with error propagation. Uses material snackbar to notify user about server errors.
   * @param snackBar Notification snackbar.
   * @returns Catched http request error.
   */
  protected baseErrorHandler(
    snackBar: MatSnackBar | null = null
  ): (error: HttpErrorResponse, caught: Observable<any>) => ObservableInput<any> {
    return (error: HttpErrorResponse, caught: Observable<any>) => {
      console.log(error);
      switch (error.status) {
        case 0: {
          snackBar?.open('Ошибка выполнения операции: отсутсвтует соединение с сервером', 'Ок', { duration: 5000 });
          break;
        }
        case 502: {
          snackBar?.open('Ошибка выполнения операции: один или несколько сервисов недоступно', 'Ок', {
            duration: 5000,
          });
          break;
        }
        case 500: {
          snackBar?.open('Ошибка выполнения операции: внутренняя ошибка сервера', 'Ок', { duration: 5000 });
          break;
        }
        case 404: {
          snackBar?.open('Ошибка выполнения операции: данные не найдены', 'Ок', { duration: 5000 });
          break;
        }
        case 422: {
          snackBar?.open('Ошибка выполнения операции: запись с таким именем уже существует', 'Ок', { duration: 5000 });
        }
      }

      return throwError(error);
    };
  }
}
