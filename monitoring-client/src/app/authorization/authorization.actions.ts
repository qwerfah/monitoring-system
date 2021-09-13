import { createAction, props } from '@ngrx/store';
import { User } from '../models/user';

export const login = createAction(
  '[Authorization Form Component] login',
  props<{ user: User }>()
);

export const logout = createAction('[Authorization Form Component] logout');
