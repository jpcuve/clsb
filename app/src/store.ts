import {configureStore, createSlice, PayloadAction} from '@reduxjs/toolkit'
import {defaultPerpetual, Perpetual} from './entities.ts'

export interface ApplicationState {
  fetching: boolean,
  perpetual: Perpetual,
}

const defaultApplicationState: ApplicationState = {
  fetching: false,
  perpetual: defaultPerpetual,
}

const applicationSlice = createSlice({
  name: 'application',
  initialState: defaultApplicationState,
  reducers: {
    updateFetching(state: ApplicationState, action: PayloadAction<boolean>){
      return {...state, fetching: action.payload}
    },
  }
})

// next functions (type: string, payload: any): only create actions
const {updateFetching} = applicationSlice.actions

export const store = configureStore({
  reducer:{
    application: applicationSlice.reducer
  }
})

export type RootState = ReturnType<typeof store.getState>

export const applicationState = {
  updateFetching: (fetching: boolean) => store.dispatch(updateFetching(fetching)),
}
