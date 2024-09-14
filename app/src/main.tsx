import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import App from './App.tsx'
// import './index.css'
import { MantineProvider } from '@mantine/core'
import '@mantine/core/styles.css'
import {StompSessionProvider} from 'react-stomp-hooks'

createRoot(document.getElementById('root')!).render(
  <StrictMode>
    <MantineProvider>
      <StompSessionProvider url={"http://localhost:8080/clsb/messaging"}>
        <App />
      </StompSessionProvider>
    </MantineProvider>
  </StrictMode>,
)
