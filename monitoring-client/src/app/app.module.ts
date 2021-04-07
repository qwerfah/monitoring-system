import { NgModule } from "@angular/core";
import { BrowserModule } from "@angular/platform-browser";

import { AuthorizationModule } from "./authorization/authorization.module";
import { RegistrationModule } from "./registration/registration.module";

import { AppRoutingModule } from "./app-routing.module";
import { AppComponent } from "./app.component";

@NgModule({
  declarations: [AppComponent],
  imports: [
    BrowserModule,
    AppRoutingModule,
    AuthorizationModule,
    RegistrationModule,
  ],
  providers: [],
  bootstrap: [AppComponent],
})
export class AppModule {}
