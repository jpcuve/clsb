import {createContext} from 'react'
import {defaultPerpetual, Perpetual} from '../entities.ts'

const PerpetualContext = createContext<Perpetual>(defaultPerpetual)

export default PerpetualContext
