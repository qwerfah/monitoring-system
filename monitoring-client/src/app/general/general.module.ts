import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MainFormComponent } from './main-form/main-form.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { FormsModule } from '@angular/forms';

import { MatSidenavModule } from '@angular/material/sidenav';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';

@NgModule({
  declarations: [MainFormComponent],
  imports: [
    CommonModule,
    BrowserAnimationsModule,
    FormsModule,
    MatSidenavModule,
    MatCheckboxModule,
    MatIconModule,
    MatButtonModule,
  ],
  exports: [MainFormComponent],
})
export class GeneralModule {}
