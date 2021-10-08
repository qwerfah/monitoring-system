import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MainFormComponent } from './main-form/main-form.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { FormsModule } from '@angular/forms';
import { environment } from 'src/environments/environment';

import { MatSidenavModule } from '@angular/material/sidenav';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { MatToolbarModule } from '@angular/material/toolbar';

import { GenealRoutingModule } from './general-routing.module';
import { MainPageComponent } from './main-page/main-page.component';
import { FullscreenLoaderComponent } from './fullscreen-loader/fullscreen-loader.component';

@NgModule({
  declarations: [MainFormComponent, MainPageComponent, FullscreenLoaderComponent],
  imports: [
    CommonModule,
    BrowserAnimationsModule,
    FormsModule,
    MatSidenavModule,
    MatCheckboxModule,
    MatIconModule,
    MatButtonModule,
    MatToolbarModule,
    GenealRoutingModule,
  ],
  exports: [MainFormComponent, MainPageComponent, FullscreenLoaderComponent],
})
export class GeneralModule {}
