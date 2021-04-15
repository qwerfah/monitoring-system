import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AuthorizationModule } from './authorization/authorization.module';
import { RegistrationModule } from './registration/registration.module';
import { GeneralModule } from './general/general.module';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MatButtonModule } from '@angular/material/button';

@NgModule({
  declarations: [AppComponent],
  imports: [
    BrowserModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    MatButtonModule,
    AuthorizationModule,
    RegistrationModule,
    GeneralModule,
  ],
  providers: [],
  bootstrap: [AppComponent],
})
export class AppModule {}
