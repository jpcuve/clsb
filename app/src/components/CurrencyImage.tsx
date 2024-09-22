import {FC} from 'react'
import {Currency, LocalTime} from '../entities.ts'

const CurrencyImage: FC<{c: string, ccy: Currency}> = props => {
  console.log(`Currency: ${JSON.stringify(props.ccy)}`)
  const height = 16
  const convertLocalTime = (localTime: LocalTime) => localTime[0] * 60 + localTime[1]
  return (
    <svg xmlns="http://www.w3.org/2000/svg" viewBox={`0 0 1440 ${height}`}>
      <line x1={0} y1={0} x2={1440} y2={0} stroke={props.c}/>
      {[...Array(24).keys()].map(it => <line x1={it * 60} y1={0} x2={it * 60} y2={height} stroke={props.c}/>)}
      <rect x={convertLocalTime(props.ccy.opening)} y={0}
            width={convertLocalTime(props.ccy.close) - convertLocalTime(props.ccy.opening)} height={height / 2}
            fill={props.c}/>
      {props.ccy.realTimeGrossSettlementPeriods.map(it => <rect x={convertLocalTime(it.init)} y={0}
                                                                width={convertLocalTime(it.done) - convertLocalTime(it.init)}
                                                                height={height} fill={props.c}/>)}
      <path
        d={`M ${convertLocalTime(props.ccy.fundingCompletionTarget) - height} 0 l ${height} ${height} l ${height} -${height}`}
        fill="black"/>
      <path
        d={`M ${convertLocalTime(props.ccy.closing) - height} ${height} l ${height} -${height} l ${height} ${height}`}
        fill="black"/>
    </svg>
  )
}

export default CurrencyImage