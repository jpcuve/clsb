export const range = (count: number) => Array.from(Array(count).keys())

const LOCAL_MOMENT_COMPONENTS: [string, number][] = [['', 4], ['-', 2], ['-', 2], [' ', 2], [':', 2], [':', 2], ['.', 3]]

export const formatLocalMoment = (moment: number[], minComponentCount: number) => {
  return [...moment, 0, 0, 0, 0, 0, 0, 0].slice(0, minComponentCount).map((it, i) => {
    const component = LOCAL_MOMENT_COMPONENTS[i] ?? ['', 0]
    return `${component[0]}${it.toString().padStart(component[1] ?? 0, '0')}`
  }).join('')
}

export function today(): [number, number, number] {
  const d = new Date()
  return [d.getFullYear(), d.getMonth() + 1, d.getDate()]
}

