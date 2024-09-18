import {configureStore, createSlice, PayloadAction} from '@reduxjs/toolkit'
import {Feedback, Perpetual} from './entities.ts'

export interface ApplicationState {
  fetching: boolean,
  perpetual?: Perpetual,
  feedbacks: Feedback[],
}

const defaultApplicationState: ApplicationState = {
  fetching: false,
  feedbacks: [],
}

const applicationSlice = createSlice({
  name: 'application',
  initialState: defaultApplicationState,
  reducers: {
    updateFetching(state: ApplicationState, action: PayloadAction<boolean>){
      return {...state, fetching: action.payload}
    },
    updatePerpetual(state: ApplicationState, action: PayloadAction<Perpetual>){
      return {...state, perpetual: action.payload}
    },
    updateFeedbacks(state: ApplicationState, action: PayloadAction<Feedback[]>){
      return {...state, feedbacks: action.payload}
    },
  }
})

// next functions (type: string, payload: any): only create actions
const {updateFetching, updatePerpetual, updateFeedbacks} = applicationSlice.actions

export const store = configureStore({
  reducer:{
    application: applicationSlice.reducer
  }
})

export type RootState = ReturnType<typeof store.getState>

export const applicationState = {
  updateFetching: (fetching: boolean) => store.dispatch(updateFetching(fetching)),
  updatePerpetual: (perpetual: Perpetual) => store.dispatch(updatePerpetual(perpetual)),
  notify: (feedback: Feedback, timeout: number = 2000) => {
    const fs = store.getState().application.feedbacks
    store.dispatch(updateFeedbacks([...fs, feedback]))
    setTimeout(() => {
      const fs = store.getState().application.feedbacks
      store.dispatch(updateFeedbacks(fs.filter(it => it !== feedback)))
    }, timeout)
  },
  notifyError: (message: string, timeout: number = 2000) => applicationState.notify({level: 'error', message}, timeout),
  notifySuccess: (message: string, timeout: number = 2000) => applicationState.notify({level: 'success', message}, timeout),
  notifyWarning: (message: string, timeout: number = 2000) => applicationState.notify({level: 'warning', message}, timeout),
  notifyInfo: (message: string, timeout: number = 2000) => applicationState.notify({level: 'info', message}, timeout),
}
