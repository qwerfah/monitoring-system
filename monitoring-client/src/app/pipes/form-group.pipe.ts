import { Pipe, PipeTransform } from '@angular/core';
import { AbstractControl, FormGroup } from '@angular/forms';

@Pipe({
  name: 'formGroup',
})
export class FormGroupPipe implements PipeTransform {
  transform(control: AbstractControl): FormGroup {
    return control as FormGroup;
  }
}
