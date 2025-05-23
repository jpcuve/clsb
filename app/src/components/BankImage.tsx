import {FC} from 'react'
import {Currency, LocalTime, Perpetual} from '../entities.ts'

const BankImage: FC<{perpetual: Perpetual, ratio: number}> = props => {
  const {perpetual, ratio} = props
  const height = 1440 / ratio
  const convertLocalTime = (localTime: LocalTime) => localTime[0] * 60 + localTime[1]
  const CurrencyImage = (ccy: Currency, index: number) => {
    return (
      <g key={ccy.iso} transform={`translate(0,${(index + 1) * height})`}>
        <line x1={0} y1={0} x2={1440} y2={0} stroke={ccy.color}/>
        {[...Array(24).keys()].map(it => <line x1={it * 60} y1={0} x2={it * 60} y2={height} stroke={ccy.color}/>)}
        <rect x={convertLocalTime(ccy.opening)} y={0} width={convertLocalTime(ccy.close) - convertLocalTime(ccy.opening)} height={height / 2} fill={ccy.color}/>
        {ccy.realTimeGrossSettlementPeriods.map((it, i) => <rect key={i} x={convertLocalTime(it.init)} y={0} width={convertLocalTime(it.done) - convertLocalTime(it.init)} height={height} fill={ccy.color}/>)}
        <path d={`M ${convertLocalTime(ccy.fundingCompletionTarget) - height / 2} 0 l ${height / 2} ${height} l ${height / 2} -${height}`} fill="white" stroke={ccy.color}/>
        <path d={`M ${convertLocalTime(ccy.closing) - height / 2} ${height} l ${height / 2} -${height} l ${height / 2} ${height}`} fill="white" stroke={ccy.color}/>
        <text x={1} y={height - 1} fontSize={height - 1} color="black">{ccy.iso}</text>
      </g>
    )
  }
  return (
    <svg xmlns="http://www.w3.org/2000/svg" viewBox={`0 0 1440 ${height * (perpetual.currencies.length + 1)}`}>
      <line x1={0} y1={0} x2={1440} y2={0} stroke="black"/>
      {[...Array(24).keys()].map(it => <line key={it} x1={it * 60} y1={0} x2={it * 60} y2={height} stroke="black"/>)}
      <rect x={convertLocalTime(perpetual.bank.opening)} y={0} width={convertLocalTime(perpetual.bank.closing) - convertLocalTime(perpetual.bank.opening)} height={height / 2} fill="grey"/>
      <path d={`M ${convertLocalTime(perpetual.bank.settlementCompletionTarget) - height / 2} 0 l ${height / 2} ${height} l ${height / 2} -${height}`} fill="white" stroke="black"/>
      {[...Array(8).keys()].map(it => <text key={it} x={1 + it * 60 * 3} y={height - 1} fontSize={height - 1} stroke="black">{`${it * 3}`}</text>)}
      {perpetual.currencies.map((it, index) => CurrencyImage(it, index))}
      <line x1={0} y1={(perpetual.currencies.length + 1) * height} x2={1440} y2={(perpetual.currencies.length + 1) * height} stroke="black"/>
      <line x1={1440} y1={0} x2={1440} y2={(perpetual.currencies.length + 1) * height} stroke="black"/>
    </svg>
  )
}

export default BankImage
