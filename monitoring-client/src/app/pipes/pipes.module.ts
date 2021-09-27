import { NgModule } from '@angular/core';
import { NotEmptyPipe } from './not-empty.pipe';

@NgModule({
  declarations: [NotEmptyPipe],
  exports: [NotEmptyPipe],
})
export class PipeModule {}
