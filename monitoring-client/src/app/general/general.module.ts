import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";
import { MainFormComponent } from "./main-form/main-form.component";
import { BrowserAnimationsModule } from "@angular/platform-browser/animations";

@NgModule({
  declarations: [MainFormComponent],
  imports: [CommonModule, BrowserAnimationsModule],
  exports: [MainFormComponent],
})
export class GeneralModule {}
