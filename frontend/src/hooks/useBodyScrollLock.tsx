import { useCallback } from 'react'

const useBodyScrollLock = () => {
  let scrollPosition = 0
  let touchStartX: number | null = null
  let touchEndX: number | null = null

  const lockScroll = useCallback(() => {
    // for IOS safari
    scrollPosition = window.pageYOffset
    document.body.style.overflow = 'hidden'
    document.body.style.position = 'fixed'
    document.body.style.top = `-${scrollPosition}px`
    document.body.style.width = '100%'
  }, [])

  const openScroll = useCallback(() => {
    // for IOS safari
    document.body.style.removeProperty('overflow')
    document.body.style.removeProperty('position')
    document.body.style.removeProperty('top')
    document.body.style.removeProperty('width')
    window.scrollTo(0, scrollPosition)
  }, [])

  const swipeLock = useCallback(() => {
    document.addEventListener('touchstart', (event: TouchEvent) => {
      // 터치 시작 지점을 저장
      touchStartX = event.touches[0].clientX
    })

    document.addEventListener('touchmove', (event: TouchEvent) => {
      // 터치 이동 중일 때 스와이프 동작을 막음
      event.preventDefault()
    })

    document.addEventListener('touchend', (event: TouchEvent) => {
      // 터치 끝 지점을 저장
      touchEndX = event.changedTouches[0].clientX

      // 스와이프 거리를 계산
      const swipeDistance = touchEndX! - touchStartX!

      // 스와이프 거리가 특정 임계값 이상인 경우, 스와이프 동작을 처리하거나 이벤트를 막을 수 있습니다.
      if (swipeDistance > 50) {
        // 오른쪽으로 스와이프 (뒤로가기 방향)
        event.preventDefault()
      } else if (swipeDistance < -50) {
        // 왼쪽으로 스와이프 (앞으로가기 방향)
        event.preventDefault()
      }
    })
  }, [])

  return { lockScroll, openScroll, swipeLock }
}

export default useBodyScrollLock
