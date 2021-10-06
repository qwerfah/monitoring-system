import { NgModule } from '@angular/core';
import { FormGroupPipe } from './form-group.pipe';
import { NotEmptyPipe } from './not-empty.pipe';

@NgModule({
  declarations: [NotEmptyPipe, FormGroupPipe],
  exports: [NotEmptyPipe, FormGroupPipe],
})
export class PipeModule {}
