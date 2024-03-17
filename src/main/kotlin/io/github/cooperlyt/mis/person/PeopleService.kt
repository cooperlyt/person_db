package org.example.io.github.cooperlyt.mis.person

import io.github.cooperlyt.commons.data.PeopleCardInfo
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.core.io.ClassPathResource
import org.springframework.stereotype.Service
import java.awt.image.BufferedImage
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.time.LocalDateTime
import java.util.*
import javax.imageio.ImageIO


@Service
@EnableConfigurationProperties(PeopleProperties::class)
class PeopleService(private val peopleCardRepository: PeopleCardRepository,
                    private val properties: PeopleProperties) {


    companion object {
        private val log = org.slf4j.LoggerFactory.getLogger(PeopleService::class.java)
    }

    suspend fun putPeopleCard(cardInfo: PeopleCardInfo): Boolean {
        return peopleCardRepository.save(cardInfo.id, cardInfo)
    }

    suspend fun getPeopleCard(id: String): Optional<PeopleCardInfo> {
        val card = peopleCardRepository.get(id)
        if (card.isPresent && properties.card.validExpire) {
            val expire = card.get().expireEnd
            if (expire != null && LocalDateTime.now().isAfter(expire)) {
                log.info("People card $id is expired")
                return Optional.empty()
            }
        }
        return card
    }

    suspend fun getPeoplePicture(id: String): ByteArray {
        return peopleCardRepository.get(id)
            .map { it.picture }
            .map(::convertedPicture)
            .orElseGet(::noPicture)

    }

    private fun convertedPicture(picture: String): ByteArray {
        val base64Picture = picture.substringAfter(",")
        val imageBytes: ByteArray = Base64.getDecoder().decode(base64Picture)


        // 将图片数据转换为 BufferedImage
        val bis = ByteArrayInputStream(imageBytes)
        val image = ImageIO.read(bis)


        // 创建一个空的 BufferedImage 以及其绘制上下文，用于转换图片格式
        val convertedImage = BufferedImage(image.width, image.height, BufferedImage.TYPE_INT_RGB)
        val g2d = convertedImage.createGraphics()
        g2d.drawImage(image, 0, 0, null)
        g2d.dispose()


        // 将 BufferedImage 转换为 byte 数组
        val bos = ByteArrayOutputStream()
        ImageIO.write(convertedImage, "jpg", bos)
        return bos.toByteArray()
    }

    private fun noPicture(): ByteArray {
        val inputStream = ClassPathResource("img/no_pic.jpg").inputStream
        return inputStream.readAllBytes()
    }


}