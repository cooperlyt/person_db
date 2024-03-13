package org.example.io.github.cooperlyt.mis.person

import io.github.cooperlyt.commons.data.PeopleCardInfo
import org.springframework.core.io.ClassPathResource
import org.springframework.stereotype.Service
import java.awt.image.BufferedImage
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.util.*
import javax.imageio.ImageIO


@Service
class PeopleService(private val peopleCardRepository: PeopleCardRepository) {

    companion object {
        private val log = org.slf4j.LoggerFactory.getLogger(PeopleService::class.java)
    }

    fun putPeopleCard(cardInfo: PeopleCardInfo): Boolean {
        return peopleCardRepository.save(cardInfo.id, cardInfo)
    }

    fun getPeopleCard(id: String): Optional<PeopleCardInfo> {
        return peopleCardRepository.get(id)
    }

    private fun convertedPicture(picture: String): ByteArray {
        val imageBytes: ByteArray = Base64.getDecoder().decode(picture)


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

    fun getPeoplePicture(id: String): ByteArray {
        return peopleCardRepository.get(id)
            .map { it.picture }
            .map(::convertedPicture)
            .orElseGet(::noPicture)

    }
}