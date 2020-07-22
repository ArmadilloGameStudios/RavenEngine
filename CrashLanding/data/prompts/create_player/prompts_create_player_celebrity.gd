{
    "name": "start_celebrity",

    "text": "What are you famous for?",

    "inputs": [
        {
            "text": "motivational speeches",

            "requirement": {
                "all": [
                    { "trait": "AGE", "value": 3, "comp": ">=" },
                    { "trait": "PERSONALITY_CHARM", "value": 2, "comp": ">=" }
                ]
            },

            "events": [
                {
                    "increment_traits": [
                        { "name": "PERSONALITY_CHARM", "value": 1 },
                        { "name": "WEALTH", "value": 1, "max": 2 }
                    ]
                }
            ],

            "prompt": "choices_create_player"
        },
        {
            "text": "conning your way into the elite",

            "requirement": {
                "all": [
                    { "trait": "PERSONALITY_CHARM", "value": 2, "comp": ">=" },
                    { "trait": "WEALTH", "value": 2, "comp": "<=" },
                    { "trait": "AGE", "value": 4, "comp": ">=" },
                    { "trait": "SKILL_DECEIT", "value": 1, "comp": ">=" }
                ]
            },

            "events": [
                {
                    "add_trait": { "name": "WEALTH", "value": 3 },
                    "increment_trait": { "name": "SKILL_DECEIT", "value": 1 }
                }
            ],

            "prompt": "choices_create_player"
        },
        {
            "text": "musical prodigy",

            "requirement": { "trait": "AGE", "value": 2, "comp": "<=" },

            "events": [
                {
                    "increment_traits": [
                        { "name": "STATUS_CELEBRITY", "value": 1 },
                        { "name": "SKILL_MUSIC", "value": 3 },
                        { "name": "WEALTH", "value": 1, "max": 2 }
                    ]
                }
            ],

            "prompt": "choices_create_player"
        },
        {
            "text": "movie actor",

            "requirement": {
                "all": [
                    { "trait": "AGE", "value": 4, "comp": ">=" },
                    { "trait": "PERSONALITY_CHARM", "value": 3, "comp": ">=" }
                ]
            },

            "events": [
                {
                    "increment_traits": [
                        { "name": "STATUS_CELEBRITY", "value": 1 },
                        { "name": "WEALTH", "value": 1, "max": 3 }
                    ]
                }
            ],

            "prompt": "choices_create_player"
        },
        {
            "text": "movie actor",

            "requirement": {
                "all": [
                    { "trait": "AGE", "value": 4, "comp": ">=" },
                    { "trait": "PERSONALITY_CHARM", "value": 2, "comp": "<=" }
                ]
            },

            "events": [
                {
                    "increment_traits": [
                        { "name": "WEALTH", "value": 1, "max": 2 }
                    ]
                }
            ],

            "prompt": "choices_create_player"
        },
        {
            "text": "child actor",

            "requirement": { "trait": "AGE", "value": 3, "comp": "<=" },

            "events": [
                {
                    "increment_traits": [
                        { "name": "STATUS_CELEBRITY", "value": 1 },
                        { "name": "WEALTH", "value": 1, "max": 2 }
                    ]
                }
            ],

            "prompt": "choices_create_player"
        }
    ]
}