import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { RouterModule } from '@angular/router';

import { GeneralModule } from './general/general.module';
import { RegistrationModule } from './registration/registration.module';
import { AuthorizationModule } from './authorization/authorization.module';
import { EquipmentModelsModule } from './equipment-models/equipment-models.module';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { environment } from '../environments/environment';
import { EquipmentInstancesModule } from './equipment-instances/equipment-instances.module';
import { MonitoringModule } from './monitoring/monitoring.module';
import { HTTP_INTERCEPTORS } from '@angular/common/http';
import { TokenInterceptor } from './helpers/token.interceptor';
import { ReportingModule } from './reporting/reporting.module';
import { AdministrationModule } from './administration/administration.module';

@NgModule({
  declarations: [AppComponent],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    GeneralModule,
    AuthorizationModule,
    RegistrationModule,
    EquipmentModelsModule,
    EquipmentInstancesModule,
    MonitoringModule,
    ReportingModule,
    AdministrationModule,
    RouterModule,
    AppRoutingModule,
  ],
  providers: [
    { provide: 'GATEWAY_URI', useValue: environment.gatewayUri },
    { provide: HTTP_INTERCEPTORS, useClass: TokenInterceptor, multi: true },
  ],
  bootstrap: [AppComponent],
})
export class AppModule {}
