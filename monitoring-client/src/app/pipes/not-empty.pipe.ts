import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'notEmpty',
})
export class NotEmptyPipe implements PipeTransform {
  transform(arrayToCheck: any[]): boolean {
    return !!(arrayToCheck && arrayToCheck.length);
  }
}
