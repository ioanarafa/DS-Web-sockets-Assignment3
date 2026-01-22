package com.example.support.service;

import org.springframework.stereotype.Service;

@Service
public class RuleBasedChatbotService {

    public String processMessage(String userMessage, String username) {
        if (userMessage == null || userMessage.trim().isEmpty()) {
            return "Hello! How can I help you today?";
        }

        String message = userMessage.toLowerCase().trim();

        if (message.matches(".*(hello|hi|hey|greetings|good morning|good afternoon|good evening).*")) {
            return "Hello " + username + "! Welcome to Energy Management System support. How can I assist you today?";
        }

        if (message.matches(".*(device|devices|what devices|list devices|show devices).*")) {
            return "You can view and manage your devices in the Devices section. Each device shows its current consumption and maximum allowed consumption. Would you like help with a specific device?";
        }

        if (message.matches(".*(consumption|energy|usage|how much|kwh|kilowatt).*")) {
            return "Energy consumption is measured in kilowatt-hours (kWh). You can view hourly consumption data for each device. If a device exceeds its maximum consumption, you'll receive an alert. Need help understanding your consumption data?";
        }

        if (message.matches(".*(alert|overconsumption|exceed|maximum|limit|warning).*")) {
            return "Overconsumption alerts are sent when a device's hourly consumption exceeds its maximum allowed limit. You'll receive real-time notifications via WebSocket. To reduce consumption, consider adjusting device settings or usage patterns.";
        }

        if (message.matches(".*(account|profile|my information|user info|settings).*")) {
            return "You can manage your account information through the User Management section. For security reasons, some changes may require administrator approval. What specific account information do you need?";
        }

        if (message.matches(".*(help|support|assistance|how to|guide|tutorial).*")) {
            return "I'm here to help! I can assist with: device management, energy consumption monitoring, understanding alerts, account settings, and general system usage. What would you like to know more about?";
        }

        if (message.matches(".*(login|log in|sign in|authentication|password|forgot password|reset).*")) {
            return "For login issues, please use the login page. If you've forgotten your password, contact your system administrator. For security, passwords cannot be reset through this chat.";
        }

        if (message.matches(".*(add device|create device|new device|register device|delete device|remove device).*")) {
            return "Device management is available in the Devices section. Administrators can add, edit, or remove devices. Each device requires a name, description, address, and maximum consumption limit. Need help with a specific device operation?";
        }

        if (message.matches(".*(status|system status|is the system|working|down|available|online).*")) {
            return "The Energy Management System is operational. All services are running normally. If you're experiencing issues, please describe the problem and I'll help troubleshoot.";
        }

        if (message.matches(".*(thank|thanks|bye|goodbye|see you|farewell|done|finished).*")) {
            return "You're welcome! If you need any further assistance, feel free to ask. Have a great day!";
        }

        if (message.matches(".*(error|problem|issue|not working|broken|failed|trouble).*")) {
            return "I'm sorry to hear you're experiencing issues. Could you please provide more details about the problem? For example: which feature isn't working, what error message you see, or when the issue started. This will help me assist you better.";
        }

        if (message.matches(".*(report|data|statistics|history|past|previous|records).*")) {
            return "Consumption data is stored hourly for each device. You can view historical consumption in the monitoring section. Reports show device consumption over time, helping you identify usage patterns and optimize energy consumption.";
        }

        return "I understand you're asking about: \"" + userMessage + "\". While I don't have a specific answer for that, I can help you with: device management, energy consumption monitoring, alerts, account settings, or general system usage. Could you rephrase your question or ask about one of these topics?";
    }
}
