package com.Qclass.demo.classes;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
public class AlertasBot extends TelegramLongPollingBot {

	//@Value("${telegram.bot.username}")
	private String botUsername ="alertasEnVivo_bot";

	//@Value("${telegram.bot.token}")
	private String botToken = "6717258560:AAGzsScMAyU21YNDzEfqqQ410uMbgjawAv8";

	@Override
	public void onUpdateReceived(Update update) {
		// TODO Auto-generated method stub
		if (update.hasMessage() && update.getMessage().hasText()) {
			Message message = update.getMessage();
			String chatId = message.getChatId().toString();
			String text = message.getText();

			System.out.println("Mensaje recibido en el chat " + chatId + ": " + text);
		}
	}

	@Override
	public String getBotUsername() {
		// TODO Auto-generated method stub
		return botUsername;
	}

	@Override
	public String getBotToken() {

		return botToken;
	}

}
