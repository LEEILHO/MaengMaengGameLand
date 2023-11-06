import { useEffect } from 'react'

const useSwipeLock = () => {
  useEffect(() => {
    let startX: number

    const handleTouchStart = (e: TouchEvent) => {
      startX = e.touches[0].clientX
    }

    const handleTouchMove = (e: TouchEvent) => {
      const endX = e.touches[0].clientX
      const deltaX = startX - endX

      // 스와이프 감지 로직
      if (deltaX > 50) {
        // 왼쪽으로 스와이프 감지 시 뒤로가기 막기
        e.preventDefault()
      }
    }

    document.addEventListener('touchstart', handleTouchStart)
    document.addEventListener('touchmove', handleTouchMove)

    return () => {
      document.removeEventListener('touchstart', handleTouchStart)
      document.removeEventListener('touchmove', handleTouchMove)
    }
  }, [])

  return null
}

export default useSwipeLock
