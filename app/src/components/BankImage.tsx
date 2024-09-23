import {FC} from 'react'
import {Currency, LocalTime, Perpetual} from '../entities.ts'

const BankImage: FC<{perpetual: Perpetual, ratio: number}> = props => {
  const {perpetual, ratio} = props
  const height = 1440 / ratio
  const convertLocalTime = (localTime: LocalTime) => localTime[0] * 60 + localTime[1]
  const CurrencyImage = (ccy: Currency, index: number) => {
    return (
      <g transform={`translate(0,${(index + 1) * height})`}>
        <line x1={0} y1={0} x2={1440} y2={0} stroke={ccy.color}/>
        {[...Array(24).keys()].map(it => <line x1={it * 60} y1={0} x2={it * 60} y2={height} stroke={ccy.color}/>)}
        <rect x={convertLocalTime(ccy.opening)} y={0} width={convertLocalTime(ccy.close) - convertLocalTime(ccy.opening)} height={height / 2} fill={ccy.color}/>
        {ccy.realTimeGrossSettlementPeriods.map(it => <rect x={convertLocalTime(it.init)} y={0} width={convertLocalTime(it.done) - convertLocalTime(it.init)} height={height} fill={ccy.color}/>)}
        <path d={`M ${convertLocalTime(ccy.fundingCompletionTarget) - height} 0 l ${height} ${height} l ${height} -${height}`} fill="white" stroke={ccy.color}/>
        <path d={`M ${convertLocalTime(ccy.closing) - height} ${height} l ${height} -${height} l ${height} ${height}`} fill="white" stroke={ccy.color}/>
        <text x={1} y={height - 1} fontSize={height - 1} color="black">{ccy.iso}</text>
      </g>
    )
  }
  return (
    <svg xmlns="http://www.w3.org/2000/svg" viewBox={`0 0 1440 ${height * (perpetual.currencies.length + 1)}`}>
      <line x1={0} y1={0} x2={1440} y2={0} stroke="black"/>
      {[...Array(24).keys()].map(it => <line x1={it * 60} y1={0} x2={it * 60} y2={height} stroke="black"/>)}
      <rect x={convertLocalTime(perpetual.bank.opening)} y={0} width={convertLocalTime(perpetual.bank.closing) - convertLocalTime(perpetual.bank.opening)} height={height / 2} fill="grey"/>
      <path d={`M ${convertLocalTime(perpetual.bank.settlementCompletionTarget) - height} 0 l ${height} ${height} l ${height} -${height}`} fill="white" stroke="black"/>
      {perpetual.currencies.map((it, index) => CurrencyImage(it, index))}
    </svg>
  )
}

export default BankImage
