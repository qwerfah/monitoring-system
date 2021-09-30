import { createAction, props } from '@ngrx/store';
import { Credentials } from '../models/credentials';

export const login = createAction('[Authorization Form Component] login', props<{ creds: Credentials }>());

export const logout = createAction('[Authorization Form Component] logout');
