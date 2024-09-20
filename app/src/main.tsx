import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import App from './App.tsx'
// import './index.css'
import { MantineProvider } from '@mantine/core'
import '@mantine/core/styles.css'
import {StompSessionProvider} from 'react-stomp-hooks'
import {createBrowserRouter, createRoutesFromElements, Route, RouterProvider} from 'react-router-dom'
import Admin from './components/Admin.tsx'
import Dashboard from './components/Dashboard.tsx'
import {Provider} from 'react-redux'
import {store} from './store.ts'

const router = createBrowserRouter(createRoutesFromElements(
  <Route path="/" element={<App/>}>
    <Route path="admin" element={<Admin/>}>
      <Route path="dashboard" element={<Dashboard/>}/>
    </Route>
  </Route>
), {
  basename: import.meta.env.VITE_APP_WEB_CONTEXT,
})

createRoot(document.getElementById('root')!).render(
  <StrictMode>
    <Provider store={store}>
      <MantineProvider>
        <StompSessionProvider url={`${import.meta.env.VITE_APP_REMOTE_HOST}${import.meta.env.VITE_APP_WEB_CONTEXT}/message`}>
          <RouterProvider router={router}/>
        </StompSessionProvider>
      </MantineProvider>
    </Provider>
  </StrictMode>,
)
