import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import App from './App.tsx'
// import './index.css'
import { MantineProvider } from '@mantine/core'
import '@mantine/core/styles.css'
import {StompSessionProvider} from 'react-stomp-hooks'
import {createBrowserRouter, createRoutesFromElements, Navigate, Route, RouterProvider} from 'react-router-dom'
import Admin from './components/Admin.tsx'
import Dashboard from './components/Dashboard.tsx'
import {Provider} from 'react-redux'
import {store} from './store.ts'
import Test from './components/Test.tsx'

const router = createBrowserRouter(createRoutesFromElements(
  <Route path="/" element={<App/>}>
    <Route index element={<Navigate to="/" replace/>}/>
    <Route path="test" element={<Test/>}/>
    <Route path="admin" element={<Admin/>}>
      <Route path="dashboard" element={<Dashboard/>}/>
    </Route>
    <Route path="*" element={<Navigate to="/" replace/>}/>
  </Route>
), {
  basename: import.meta.env.VITE_APP_WEB_CONTEXT,
})

createRoot(document.getElementById('root')!).render(
  <StrictMode>
    <Provider store={store}>
      <MantineProvider defaultColorScheme="auto">
        <StompSessionProvider url={`${import.meta.env.VITE_APP_REMOTE_HOST}${import.meta.env.VITE_APP_WEB_CONTEXT}/messaging`}>
          <RouterProvider router={router}/>
        </StompSessionProvider>
      </MantineProvider>
    </Provider>
  </StrictMode>,
)
